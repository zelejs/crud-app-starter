import React, { useState, useEffect } from 'react';
import { Th } from '@chakra-ui/react'

export default function Index(props) {
    const { items } = props
    
    const keys = Object.keys(items)

    return (
        <>
            {
                    <Th textAlign='center' w='150px'  >{items[keys]}</Th>
            }
        </>
    )
}