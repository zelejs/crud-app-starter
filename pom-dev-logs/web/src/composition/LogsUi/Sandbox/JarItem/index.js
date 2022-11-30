import React from 'react';
import { Flex, Center, Box, Stack, Spacer } from "@chakra-ui/react";
require('./index.less');

/**
 * 
 */
export default function (props) {

    const { value, index=0 } = props;

    return (
        <div className="textColor">
            <Flex h="19px">
                {/* <Center w="40px">
                    {index+1}
                </Center> */}
                {/* <Center axis='vertical' >
                  {`${index+1}.   ${value}`}
                </Center> */}
                <Center axis='vertical' >
                  {`${value}`}
                </Center>
                <Center w="10px">
                </Center>
            </Flex>
        </div>
    )
    
 

}