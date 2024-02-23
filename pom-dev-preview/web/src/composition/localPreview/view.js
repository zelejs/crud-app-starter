import React, { useState } from 'react';
import { VStack, Box } from '@chakra-ui/react';
import { AutoLayout } from 'zero-element-boot';
import Title from 'zero-element-boot/lib/components/presenter/Title';
import mockData from '@/composition/localPreview/mockData';
import layout from './layout.js';

export default function Index(props) {

    const { previewLayout } = props;

    return (
        previewLayout ? (
            <VStack spacing={10}>
                <Box w={'100%'}>
                    <VStack>
                        <Box w={'100%'} marginBottom={'6px'}>
                            <Title content="Classics" />
                        </Box>
                        <Box w={'100%'} marginLeft={'1px'}>
                            <AutoLayout items={mockData.avatarList} layout={{ ...layout, ...previewLayout.iconLayoutConfig }} />
                        </Box>
                    </VStack>
                </Box>

                <Box w={'100%'}>
                    <VStack>
                        <Box w={'100%'} marginBottom={'6px'}>
                            <Title content="Happy Birthday" />
                        </Box>
                        <Box w={'100%'} marginLeft={'1px'}>
                            <AutoLayout items={mockData.imgList} layout={{ ...layout, ...previewLayout.icon2LayoutConfig }} />
                        </Box>
                    </VStack>
                </Box>

                <Box w={'100%'}>
                    <VStack>
                        <Box w={'100%'} marginBottom={'6px'}>
                            <Title content="质量等级" />
                        </Box>
                        <Box w={'100%'} marginLeft={'1px'}>
                            <AutoLayout items={mockData.cardList} layout={{ ...layout, ...previewLayout.cardLayoutConfig }} />
                        </Box>
                    </VStack>
                </Box>

                <Box w={'100%'}>
                    <VStack>
                        <Box w={'100%'} marginBottom={'6px'}>
                            <Title content="文字" />
                        </Box>
                        <Box w={'100%'}>
                            <AutoLayout items={mockData.textList} layout={{ ...layout, ...previewLayout.textLayoutConfig }} />
                        </Box>
                    </VStack>
                </Box>
            </VStack>
        ) : <></>

    )

}