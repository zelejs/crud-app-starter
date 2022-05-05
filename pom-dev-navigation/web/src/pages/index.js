import React, { useState, useEffect } from 'react';
import Nagation from '@/pages/nagation'

import { setEndpoint, setToken } from 'zero-element-boot/lib/components/config/common';
export default function index (props) {


  // Nagation
  if (process.env.NODE_ENV == 'development') {
    setEndpoint('http://app1.console.smallsaas.cn:8001');
    // setToken('')
  }
  return (

    <Nagation {...props} />

  )



}