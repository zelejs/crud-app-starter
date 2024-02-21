import React, { useState } from 'react';
import { ChakraProvider } from '@chakra-ui/react';
import Selectors from '@/composition/selectors';


export default function Index(props) {

  return (
    <ChakraProvider>
      <Selectors {...props}/>
    </ChakraProvider>
  )

}