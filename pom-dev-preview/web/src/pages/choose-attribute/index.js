import React, { useState, useEffect } from 'react';
import { ChakraProvider } from '@chakra-ui/react';
import ListPage from '@/composition/chooseAttributeListPage/Sandbox';


export default function Index(props) {
    
  return (
    <ChakraProvider>
      <div style={{padding: '8px'}}>
        <ListPage {...props}/>
        </div>
    </ChakraProvider>
  )
}