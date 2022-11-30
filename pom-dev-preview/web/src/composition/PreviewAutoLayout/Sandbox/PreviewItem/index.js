import React from 'react';
import { Flex, Center, Box, VStack, Spacer } from "@chakra-ui/react";
require('./index.less');

/**
 * 
 */
export default function (props) {

    const { label, value, index=0 } = props;

    return (
        
        <VStack  w={120} align='flex-start'>
            <Box w="120">
                {label}
            </Box>
            <Box >
                {value}
            </Box>
        </VStack>
        
    )

}