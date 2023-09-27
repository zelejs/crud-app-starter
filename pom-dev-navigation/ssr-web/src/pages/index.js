import React from 'react';
// import { ChakraProvider } from "@chakra-ui/react";
// import NavUi from '@/composition/NavUi'
import { history } from 'umi';

export default function index (props) {

  history.push('/nav-ui');
  return (
    // <ChakraProvider>
    //   <NavUi {...props} />
    // </ChakraProvider>
    <></>
  )

}