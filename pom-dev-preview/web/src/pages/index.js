import React from 'react';
import { ChakraProvider } from "@chakra-ui/react";
import PreviewAutoLayout from '@/composition/PreviewAutoLayout/Sandbox'

export default function index (props) {

  return (
    <ChakraProvider>
      <PreviewAutoLayout {...props} />
    </ChakraProvider>
  )

}