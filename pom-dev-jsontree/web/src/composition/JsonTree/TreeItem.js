import { Box, Center, Flex, Stack } from '@chakra-ui/layout';
import React from 'react';
import PageSection from 'zero-element-boot-plugin-theme/lib/components/text/pageSectionTitle/PageSectionTitle';
import TagIndicator from 'zero-element-boot/lib/components/indicator/TagIndicator'

export default function index(props) {

    const { keyName, ...data } = props

    const values = data[keyName]

    // console.log('props ==', props)
    // console.log('keyNamessss ==', keyName)

    return (
        <>
            <Flex h='30px' bg='' margin='2px 0 0 0'>
                <Center h='100%' margin='0 8px 0 0 '>
                    <svg t="1662005424492" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="72865" width="18" height="18"><path d="M170.666667 469.333333h682.666666v85.333334H170.666667z" fill="#cdcdcd" p-id="72866"></path></svg>
                </Center>
                <Center h='100%'>
                    <PageSection>
                        {keyName}：
                    </PageSection>
                </Center>
                <Center h='100%'>
                    {/* <Center h='28px' color='#d3455b' border='2px solid #efbcc4' borderRadius='8px' padding='0 16px' margin='4px' > */}
                    <Center h='28px' color='#d3455b' border='2px solid #efbcc4' borderRadius='8px' padding='0 16px' margin='4px' >
                        <PageSection>
                            {values}
                        </PageSection>
                    </Center>
                </Center>

            </Flex>
        </>

    )
}