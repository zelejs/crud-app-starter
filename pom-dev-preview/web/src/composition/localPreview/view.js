import React, { useState } from 'react';
import { VStack, Box } from '@chakra-ui/react';
import Title from 'zero-element-boot/lib/components/presenter/Title';
import LocalPreview from '@/composition/localPreview';
import mockData from '@/composition/localPreview/mockData';
import layout from './layout.json';

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
                            <LocalPreview items={mockData.avatarList} configLayout={{ ...layout, ...previewLayout.iconLayoutConfig }} />

                        </Box>
                    </VStack>
                </Box>

                <Box w={'100%'}>
                    <VStack>
                        <Box w={'100%'} marginBottom={'6px'}>
                            <Title content="Happy Birthday" />
                        </Box>
                        <Box w={'100%'} marginLeft={'1px'}>
                            <LocalPreview items={mockData.imgList} configLayout={{ ...layout, ...previewLayout.icon2LayoutConfig }} />
                        </Box>
                    </VStack>
                </Box>

                <Box w={'100%'}>
                    <VStack>
                        <Box w={'100%'} marginBottom={'6px'}>
                            <Title content="质量等级" />
                        </Box>
                        <Box w={'100%'} marginLeft={'1px'}>
                            <LocalPreview items={mockData.cardList} configLayout={{ ...layout, ...previewLayout.cardLayoutConfig }} />
                        </Box>
                    </VStack>
                </Box>

                <Box w={'100%'}>
                    <VStack>
                        <Box w={'100%'} marginBottom={'6px'}>
                            <Title content="文字" />
                        </Box>
                        <Box w={'100%'}>
                            <LocalPreview items={mockData.textList} configLayout={{ ...layout, ...previewLayout.textLayoutConfig }} />
                        </Box>
                    </VStack>
                </Box>
            </VStack>
        ) : <></>

    )

}