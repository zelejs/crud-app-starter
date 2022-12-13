import React from 'react';
import { ChakraProvider } from "@chakra-ui/react";
import LogsUi from '@/composition/LogsUi/Sandbox'

export default function index (props) {

  return (
    <ChakraProvider>
      <LogsUi {...props} />
    </ChakraProvider>
  )

}