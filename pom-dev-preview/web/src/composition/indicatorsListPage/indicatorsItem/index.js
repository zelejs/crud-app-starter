import React from 'react';
import { Flex, Center, Box, VStack, Spacer } from "@chakra-ui/react";
require('./index.less');

/**
 * 
 */
export default function (props) {

    const { label, value, index=0 } = props;

    return (
        
        <VStack align='center'>
            <Box fontSize={'16px'} fontWeight={'bold'}>
                {label}
            </Box>
            <Box >
                {value}
            </Box>
        </VStack>
        
    )

}