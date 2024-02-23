import React from 'react';
import { ChakraProvider } from '@chakra-ui/react';
import Carts from '@/composition/carts';


export default function Index (props) {

   return (
    <ChakraProvider>
      <Carts {...props}/>
    </ChakraProvider>
  )

}