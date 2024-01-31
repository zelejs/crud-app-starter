import React from 'react';
import { ChakraProvider } from '@chakra-ui/react';
import ModuleListPage from '@/composition/cartsListPage/Sandbox';

export default function Index (props) {

  return (
    <ChakraProvider>
      <div style={{padding: '8px'}}>
        <ModuleListPage {...props}/>
      </div>
    </ChakraProvider>
  )

}