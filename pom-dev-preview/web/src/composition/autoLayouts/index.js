import React, { useState, useEffect } from 'react';
import { VStack, Box, HStack, Button, useToast, createIcon } from '@chakra-ui/react';
import { AutoLayout } from 'zero-element-boot'
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
// import LocalPreview from '@/composition/localPreview';
// import PreviewFetch from '@/composition/localPreview/indeFetch';
import PreviewFetch2 from '@/composition/localPreview/indexManage';
import categoryListLayout from '@/composition/presenters/CategoryList/layout';
import AddPresenter from '@/composition/AddPresenter';
import { useForceUpdate } from 'zero-element-boot/lib/components/hooks/lifeCycle';
import _ from 'lodash';

export default function AutoLayouts(props) {

    const { id, status, isSwitch } = props
    const api = '/openapi/lc/module?componentOption=autolayout&pageNum=1&pageSize=100'
    const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/160'

    const [ currentApi, setCurrentApi ] = useState(api)
    const [ currentLayoutApi, setCurrentLayoutApi ] = useState(layoutApi)
    // const [previewData, setPreviewData] = useState('')

    const [ previewAutoLayoutId, setPreviewAutoLayoutId ] = useState('')
    const [isAddClick, setIsAddClick] = useState(false)
    const [currentCategoryName, setCurrentCategoryName] = useState('element')
    const [moduleName, setModuleName] = useState('')
    const [containerHeight, setContainerHeight] = useState(window.innerHeight)
    const toast = useToast()


    useEffect(() => {
        if(isSwitch){
            setContainerHeight(window.innerHeight - 40)
        }else{
            setContainerHeight(window.innerHeight)
        }
    }, [isSwitch])

    const onComponentItemClick = (item) => {
        // console.log('item = ', item)
        setIsAddClick(false)
        setPreviewAutoLayoutId()
        if (item.isSelected) {
        //     setPreviewData({
        //         ___presenter2: {
        //             xname: item.componentType,
        //             props: item.componentProps
        //         }
        //     })
        setTimeout(_=>{
            setModuleName(item.moduleName)
            setPreviewAutoLayoutId(item.id)
        },100)
            
        }
    }

    const onCateItemClick = (item) => {
        setCurrentApi('')
        setCurrentLayoutApi('')
        setCurrentCategoryName(item.name)
        setIsAddClick(false)
        setPreviewAutoLayoutId()
    }

    //新增
    const addNewClick = () => {
        if(currentCategoryName){
            setIsAddClick(true)
            setPreviewAutoLayoutId()
        }else{
            toastTips('请选择分类')
        }
    }

    //新增回调事件
    const cb = (status) => {
        if (status === 'success') {
            const nApi = _.cloneDeepWith(currentApi)
            const nLayoutApi = _.cloneDeepWith(currentLayoutApi)
            setCurrentApi()
            setCurrentLayoutApi()
            setIsAddClick(false)
            setTimeout(() => {
                setCurrentApi(nApi)
                setCurrentLayoutApi(nLayoutApi)
            },100)
            useForceUpdate()
        }else if (status === 'error'){
            setIsAddClick(false)
          }
    }

    // 菜单回调
    const menuCb = (status) => {
        console.log()
        if(status){
            setCurrentApi('')
            setCurrentLayoutApi('')
            setCurrentCategoryName(currentCategoryName)
        }
    }

    // tips
    function toastTips(text, status = 'success') {
        toast({
            title: text,
            description: "",
            status: status,
            duration: 3000,
            isClosable: true,
            position: 'top'
        })
    }

    return (
        <VStack align='stretch' spacing='0'>
            <HStack spacing={'0'}>
                <Box style={{ width: '500px', height: '100vh', display: 'flex', flexDirection:'column', justifyContent: 'flex-start', alignItems: 'flex-start', padding: '0px 8px 0px 8px', background: '#fff' }}>
                    {/* { isSwitch && (
                        <Button style={{marginTop: '8px', padding: '8px'}} onClick={addNewClick}>新增</Button>
                    )} */}
                    {
                        currentApi && currentLayoutApi && (
                            <PreviewAutoLayout 
                                layoutApi={currentLayoutApi} 
                                api={currentApi} 
                                onItemClick={onComponentItemClick} 
                                onAddNewClick={addNewClick} 
                                isSwitch={isSwitch}
                                containerHeight={containerHeight}
                            />
                        )
                    }
                </Box>
                <Box style={{ width: '100%', height: '100vh' }} background={'#EDECF1'}>
                    {
                        isAddClick ? (
                            <Box style={{ height: '100vh', padding: '8px', marginLeft: '6px', background: '#fff' }}>
                                <AddPresenter cb={cb} moduleType={''} />
                            </Box>
                        ):(
                            previewAutoLayoutId ? (
                            <Box style={{ width: '100%', height: '100vh', padding: '8px', display: 'flex', justifyContent: 'flex-start' }} background={'#EDECF1'}>
                                {/* <LocalPreview previewData={previewData} type='presenter' /> */}
                                {/* <PreviewFetch previewAutoLayoutId={previewAutoLayoutId}/> */}
                                <PreviewFetch2  previewAutoLayoutId={previewAutoLayoutId} moduleName={moduleName} cb={menuCb}/>
                            </Box>
                            ) : <></>
                        )
                        
                    }
                </Box>
            </HStack>
        </VStack>
    )

}