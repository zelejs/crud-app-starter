import React, { useState, useEffect } from 'react';
import {
    Table,
    Thead,
    Tbody,
    Tr,
    // Th,
    // Td,
    TableContainer,
    ChakraProvider
} from '@chakra-ui/react'
import Td from './td'
import Th from './th'



/**
 * 
 * @param {datas } datas  数据
 * @param {headers } headers  表头
 * 
 */

export default function Index(props) {

    const { datas, headers } = props
    console.log('props =', props)
    return (
        <ChakraProvider>
            <TableContainer>
                <Table variant='simple' >
                    <Thead>
                        <Tr>
                            {
                                (headers.map((item, i) => (
                                    <Th items={item} w='150px' textAlign='center' key={i} />
                                )))
                            }
                        </Tr>
                    </Thead>
                    <Tbody>
                        {
                            datas && datas.length > 0 ?
                                (datas && datas.map((items, i) => (
                                    <Tr key={i} >
                                        <Td items={items} />
                                    </Tr>
                                ))
                                )
                                : <></>

                        }
                    </Tbody>
                </Table>
            </TableContainer>
        </ChakraProvider>
    )
}