import { Center, Flex, Stack } from '@chakra-ui/layout';
import React, { useState } from 'react';
import Tree from './index'
import PageSection from 'zero-element-boot-plugin-theme/lib/components/text/pageSectionTitle/PageSectionTitle';

export default function index(props) {

    const { keyName, ...data } = props

    const values = data[keyName]
    const [ isShowObj, setIsShowObj ] = useState(true)

    // console.log('data ==', data)
    // console.log('keyName ==', keyName)
    // 获取Value值
    function getValue(props, keyName) {
        return props[keyName]
    }
    const keys = Object.keys(props)

    function showObj(){
        const status = !isShowObj
        setIsShowObj(status)
    }

    return (
        <>
            {/* <Flex> */}
            <Flex h='30px' bg='#f5f5f5' margin=' 2px 0 0 0'>
                <Center h='100%' margin='0 8px 0 0' onClick={() => showObj()}>
                    {isShowObj ? (
                        <svg t="1662005424492" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="72865" width="18" height="18"><path d="M170.666667 469.333333h682.666666v85.333334H170.666667z" fill="#cdcdcd" p-id="72866"></path></svg>
                    ):(
                        <svg t="1662005256045" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="71804" width="18" height="18"><path d="M469.333333 469.333333V170.666667h85.333334v298.666666h298.666666v85.333334h-298.666666v298.666666h-85.333334v-298.666666H170.666667v-85.333334h298.666666z" fill="#cdcdcd" p-id="71805"></path></svg>
                    )}
                </Center>
                <Center h='100%'>
                    <PageSection>
                        {keyName}：
                    </PageSection>
                </Center>
            </Flex>
                <Stack margin='0 0 0 20px'  spacing='0' display={isShowObj ? '' : 'none'}>
                    <Tree  {...getValue(props, keyName)} />
                </Stack>
         
        </>

    )
}

