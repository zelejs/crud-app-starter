import React, { useState } from 'react';
import { ChakraProvider, VStack, HStack, Box } from '@chakra-ui/react';
import { history } from 'umi';
import Indicators from '@/composition/indicators';

export default function Index(props) {

  return (
    <ChakraProvider>
      <Indicators {...props}/>
    </ChakraProvider>
  )

}