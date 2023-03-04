import React, { useState, useEffect } from 'react';
import { Td } from '@chakra-ui/react'

export default function Index(props) {
    const { items } = props
    
    const data =Object.values(items)

    return (
        <>
            {
                data.map((item, i) => (
                    <Td textAlign='center' w='150px' key={i} >{item}</Td>
                )
                )
            }
        </>
    )
}