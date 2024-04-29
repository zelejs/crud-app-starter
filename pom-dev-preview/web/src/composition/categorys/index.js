import React, { useState, useEffect } from 'react';
import { HStack, Box } from '@chakra-ui/react';
import { AutoLayout } from 'zero-element-boot';
import categoryListLayout from './CategoryList/layout';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import { WxPage, AddNewContainer } from 'zero-element-boot/lib/components/container';
import { DefaultGridLayoutList } from 'zero-element-boot/lib/components/list'
import PlacementIndicaor from 'zero-element-boot/lib/components/indicator/PlacementIndicator';

import AddNewModal from 'zero-element-boot/lib/components/modalComponent/AddNewModal';
import AddPresenter from '@/composition/AddPresenter'

export default function Categorys(props) {

    const { } = props;

    const [ _moduleType, setModuleType ] = useState('');

    useEffect(() => {
        setModuleType('web');
    }, [])

    const onCateItemClick = (item) => {
        setModuleType('')
        setTimeout(() => {
            setModuleType(item.name);
        }, 100);
    }

    const PreviewPage = () => {

        const config = {
            listApi: `/openapi/lc/module?componentOption=presenter&pageNum=1&pageSize=100&moduleType=${_moduleType}`,
            converter: {
                moduleName: 'layoutName'
            },
            cartConfig: {
                ratio: 1,
                overflow: 'auto',
            }
        }

        const _Indicator = () => {
            //svg图片
            const IconSvg = () => {
              return (
                <svg t="1712720955253" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="15898" width="20" height="20"><path d="M824.487092 906.287384 100.255236 906.287384c-24.939113 0-44.890404-19.951291-44.890404-44.890404L55.364832 137.165124c0-24.939113 19.951291-44.890404 44.890404-44.890404l319.719435 0c8.479299 0 14.963468 6.48417 14.963468 14.963468s-6.48417 14.963468-14.963468 14.963468L100.255236 122.201656c-8.479299 0-14.963468 6.48417-14.963468 14.963468l0 723.733074c0 8.479299 6.48417 14.963468 14.963468 14.963468l723.733074 0c8.479299 0 14.963468-6.48417 14.963468-14.963468l0-343.660984c0-8.479299 6.48417-14.963468 14.963468-14.963468s14.963468 6.48417 14.963468 14.963468l0 343.660984C869.377496 885.837311 848.927423 906.287384 824.487092 906.287384z" p-id="15899"></path><path d="M854.414028 410.497808c-8.479299 0-14.963468-6.48417-14.963468-14.963468L839.45056 122.201656l-286.799805 0c-8.479299 0-14.963468-6.48417-14.963468-14.963468s6.48417-14.963468 14.963468-14.963468l316.726741 0 0 303.25962C869.377496 404.013639 862.394545 410.497808 854.414028 410.497808z" p-id="15900"></path><path d="M400.02338 577.091086c-3.990258 0-7.481734-1.496347-10.474428-4.48904-5.985387-5.985387-5.985387-15.46225 0-20.948855l454.390648-454.390648c5.985387-5.985387 15.46225-5.985387 20.948855 0 5.985387 5.985387 5.985387 15.46225 0 20.948855l-454.390648 454.390648C407.505114 575.594739 403.514856 577.091086 400.02338 577.091086z" p-id="15901"></path></svg>
              )
            }
            return (
              <Box cursor={'pointer'} padding={'0 20px'} >
                <IconSvg />
              </Box>
            )
          }
          
        
          const onPreviewClick = (data) => {
            const w = window.open('about:blank');
            w.location.href = `/#/preview?layoutName=${data.layoutName}`
        }

        return (
            <Box 
                width={'100%'} overflow={'auto'}
                sx={{
                    '::-webkit-scrollbar': {
                        width: '1px',
                    },
                    '::-webkit-scrollbar-track': {
                    background: '#E1E1E2',
                    },
                    '::-webkit-scrollbar-thumb': {
                    background: '#b0b0b0',
                    borderRadius: '10px',
                    }
                }}
            >
                <AddNewContainer {...config}>
                    <DefaultGridLayoutList columns={_moduleType === 'app'? 3: 1} hasCart={false} hasIndicator={false}>
                        <PlacementIndicaor Indicator={_Indicator} alignment="topright" onPreviewTriggered={onPreviewClick}>
                            <WxPage device={_moduleType === 'app'? 'wx': 'pc'}>
                                <PreviewAutoLayout previewAddNew={false}/>
                            </WxPage>
                        </PlacementIndicaor>
                    </DefaultGridLayoutList>
                    <AddNewModal moduleType={_moduleType}>
                        <AddPresenter/>
                    </AddNewModal>
                </AddNewContainer>
            </Box>
        )
    }
    
    return (
        <HStack spacing={1}>
            <Box style={{ width: '200px', height: '100vh', padding: '10px 20px', background: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <AutoLayout layout={categoryListLayout} isScroll={false} onItemClick={onCateItemClick} />
                </Box>
                <Box style={{ width: '100%', height: '100vh', display: 'flex', flexDirection:'column', justifyContent: 'flex-start', alignItems: 'flex-start', padding: '0px 8px 0px 8px', background: '#fff' }}>
                    { _moduleType ? (
                        <PreviewPage/>
                    ):<></>}
                </Box>
        </HStack>
    )
}