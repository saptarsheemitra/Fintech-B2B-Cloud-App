import React from 'react';
import logo from '../hrclogo.svg';
import logo2 from '../logos/abclogo.svg';
import '../cssfile/Heading.css';

export default function Header(){
  return (
    <div className="heading" >
      <img className='abclogo' src={logo2} alt="logo" />
      <img className='hrclogo' src={logo} alt="logo" />
      <text className='text-below' style={{color: 'white'}}><h2>LIST INVOICE</h2></text>
    </div>
    
  );
};