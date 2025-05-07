import React from 'react';
import { ChakraProvider } from "@chakra-ui/react";
import NavUi from '@/composition/NavUi'

export default function index (props) {

  return (
    <ChakraProvider>
      <NavUi {...props} />
    </ChakraProvider>
  )

}