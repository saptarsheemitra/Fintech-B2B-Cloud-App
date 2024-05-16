import pickle
import numpy as np
import pandas as pd


def predict(df):

    if isinstance(df, dict) and all(isinstance(v, (int, float, str)) for v in df.values()):
        df = {k: [v] for k, v in df.items()}
    df = pd.DataFrame(df)
    df['orderCreationDate'] = df['orderCreationDate'].str.replace('-', '')
    df['requestedDeliveryDate'] = df['requestedDeliveryDate'].str.replace(
        '-', '')
    df['orderCreationDate'] = pd.to_datetime(
        df['orderCreationDate'], format="%Y%m%d", errors='coerce')
    df['requestedDeliveryDate'] = pd.to_datetime(
        df['requestedDeliveryDate'], format="%Y%m%d", errors='coerce')

    conversion_rates = {
        'USD': 1.0,
        'EUR': 0.93,
        'CAD': 1.35,
        'AUD': 1.52,
        'GBP': 0.81,
        'PLN': 4.17,
        'AED': 3.67,
        'MYR': 4.59,
        'CHF': 0.90,
        'RON': 4.60,
        'HKD': 7.83,
        'CZK': 21.94,
        'SGD': 1.35,
        'HU1': 345.117,
        'BHD': 0.38,
        'NZD': 1.63,
        'SAR': 3.75,
        'QAR': 3.64,
        'KWD': 0.31,
        'SEK': 10.67
    }

    # Convert "orderAmount" column to numeric
    df['orderAmount'] = pd.to_numeric(df['orderAmount'], errors='coerce')

    # Creating a new column "amount_in_usd" and convert currency amounts to USD
    df['amount_in_usd'] = df.apply(
        lambda row: row['orderAmount'] * conversion_rates.get(row['orderCurrency'], 1.0), axis=1)

    df['amount_in_usd'].sort_values(ascending=False)/1000000
    # print(df['amount_in_usd'])

    # df[['customerNumber','companyCode']].head()

    df['unique_cust_id'] = df['customerNumber'].astype(
        str)+df['companyCode'].astype(str)
    # df['unique_cust_id'].nunique()

    # print(df['unique_cust_id'])

    def removeOutlier(group, net_amount_col):
        mean, std = np.mean(group[net_amount_col]), np.std(
            group[net_amount_col])
        group[net_amount_col] = np.where(
            group[net_amount_col] > mean+3*std, mean, group[net_amount_col])
        return group

    df_amount_removed_3 = df.groupby('unique_cust_id').apply(
        lambda x: removeOutlier(x, 'amount_in_usd'))

    df_amount_removed = df_amount_removed_3.copy()
    del df_amount_removed_3

    import xgboost as xgb
    from sklearn.metrics import r2_score, mean_squared_error, mean_absolute_error, mean_squared_error
    from sklearn.feature_selection import RFE

    list_of_cols = ["companyCode", "customerNumber",
                    "amount_in_usd", "orderCreationDate", "orderCurrency"]
    customer_id_col = 'unique_cust_id'
    net_amount_col = 'amount_in_usd'
    create_date_col = 'orderCreationDate'
    model_1 = df_amount_removed[["unique_cust_id",
                                 "orderCreationDate", "amount_in_usd"]]

    def create_more_lags(melt, lags, ffday, customer_id_col, create_date_col, net_amount_col):
        for i in range(ffday, lags+1):
            melt.loc[:, 'Last-'+str(i)+'day_Sales'] = melt.groupby(
                [customer_id_col])[net_amount_col].shift(i)

        melt = melt.reset_index(drop=True)

        for i in range(ffday, lags+1):
            melt['Last-'+str(i)+'day_Diff'] = melt.groupby([customer_id_col]
                                                           )['Last-'+str(i)+'day_Sales'].diff()
        melt = melt.fillna(0)
        return melt

    def add_datepart(df, fldname, drop):
        fld = df[fldname]
        if not np.issubdtype(fld.dtype, np.datetime64):
            df[fldname] = fld = pd.to_datetime(fld,
                                               infer_datetime_format=True)
        for n in ('Year', 'Month', 'Week', 'Day', 'Dayofweek',
                  'Dayofyear', 'Is_month_end', 'Is_month_start',
                  'Is_quarter_end', 'Is_quarter_start', 'Is_year_end',
                  'Is_year_start'):
            df[n] = getattr(fld.dt, n.lower())

        if drop:
            df.drop(fldname, axis=1, inplace=True)

        return df

    model_1 = create_more_lags(
        model_1, 60, 1, customer_id_col, create_date_col, net_amount_col)

    # date feat like is_year,is_month etc
    model_1 = add_datepart(model_1, create_date_col, False)

    # print(model_1)

    features = model_1.drop(
        [net_amount_col, create_date_col, customer_id_col], axis=1).columns

    model = pickle.load(open("finalized_model.sav", 'rb'))

    prediction = np.expm1(model.predict(model_1[features]))

    return prediction
