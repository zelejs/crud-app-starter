import React, { useState, useEffect } from 'react';
import { ChakraProvider } from "@chakra-ui/react";
import ApisUi from '@/composition/ApisUi/Sandbox'

export default function index (props) {

  return (
    <div style={{margin: '0 10px'}}>
      <ChakraProvider>
        <ApisUi {...props} />
      </ChakraProvider>
    </div>
  )

}