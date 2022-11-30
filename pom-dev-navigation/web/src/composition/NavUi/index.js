import React, { useState, useEffect, useRef } from 'react';
import {
    ChakraProvider, Box, VStack, Spinner, Switch, FormControl, FormLabel, Tabs, TabList, TabPanels, Tab, TabPanel,
    Button, useTab, useMultiStyleConfig, Image
} from "@chakra-ui/react";
import { useForm } from 'react-hook-form';

import { AutoLayout } from 'zero-element-boot/lib/components';
import TabsCompox from './compx/tabsComps';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

import layout from './layout';

require('./index.less')

export default function Index(props) {

    const { } = props;

    const [navCateListData, setNavCateListData] = useState([])
    const [listData, setListData] = useState([])
    const [isLoading, setLoading] = useState(false)
    const [switchStatus, setSwitchStatus] = useState(false)
    const [tabIndex, setTabIndex] = useState(0)


    let navListApi = '/api/pub/data/services/navigation';
    let navApi = '/api/pub/data/services/navCategory?sort=sortNum&orderBy=ASC';

    useEffect(() => {
        console.log('首次加载')
        fetchNavCategoryData(navApi, {})
    }, []);

    let layoutData = '';
    const layoutJsonPath = '';
    const localLayoutJson = layout;

    if (layoutJsonPath) {
        layoutData = { path: layoutJsonPath };
    } else {
        layoutData = localLayoutJson;
    }
    const config = {
        items: listData,
        layout: layoutData
    };

    //获取分类列表信息
    const fetchNavCategoryData = (api, queryData) => {
        setLoading(true)
        let newNavCateList = []
        return promiseAjax(api, queryData).then(resp => {
            if (resp && resp.code === 200) {
                newNavCateList = resp.data.records;

                //-1:新增  -2删除
                newNavCateList.push({id:'-1'})
                newNavCateList.push({id:'-2'})
                setNavCateListData(newNavCateList);
                setLoading(false)
            } else {
                console.error('获取列表数据失败 ==', resp)
            }
        }).finally(_ => {
            setLoading(false)
            if(newNavCateList.length > 0){
                setTabIndex(newNavCateList[0].id)
                fetchData(navListApi, { typeId: newNavCateList[0].id })
            }
        });
    }

    //获取列表信息
    const fetchData = (api, queryData) => {
        setLoading(true)
        return promiseAjax(api, queryData).then(resp => {
            if (resp && resp.code === 200) {
                const list = resp.data.records;
                setListData(list);
                setLoading(false)
            } else {
                console.error('获取列表数据失败 ==', resp)
            }
        }).finally(_ => {
            setLoading(false)
        });
    }

    //列表item点击事件
    const onNavItemClick = (item) => {
        const id = item.id;
        // console.log('id = ', id)
        // alert(`选择的用户id为: ${id}`)
        //点击跳转页面
        if (item.path.indexOf('http') != -1) {
          // window.location.replace(item.path)
    
          // history.push(url);
          const w = window.open('about:blank');
          w.location.href = item.path
        //   console.log(item.path);
    
        } else {
          const w = window.open('about:blank');
          const host = getEndpoint || location.host
          w.location.href = host + item.path
    
        //   console.log(host);
    
        }
    }

    //列表item回调函数
    const callback = (value) => {
        if (value) {
            const queryData = {
                typeId: tabIndex
            }
            fetchData(navListApi, queryData)
        }
    }

    //列表item回调函数
    const tabscallback = (value) => {
        if (value) {
            setNavCateListData([])
            setListData([])
            fetchNavCategoryData(navApi, {})
        }
    }

    //开启/关闭 编辑按钮
    const handleChange = () => {
        const status = !switchStatus;
        setSwitchStatus(status)
        if(!status){
            setNavCateListData([])
            setListData([])
            fetchNavCategoryData(navApi, {})
        }
    }

    //tab切换
    const switchTab = (item, index) => {
        if (index != tabIndex) {
            setTabIndex(index)
            const queryData = {
                typeId: item.id
            }
            fetchData(navListApi, queryData)
        }
    }

    //自定义tab按钮
    // const CustomTab = React.forwardRef((props, ref) => {
    //     // 1. Reuse the `useTab` hook
    //     const tabProps = useTab({ ...props, ref })
    //     const isSelected = !!tabProps['aria-selected']

    //     // 2. Hook into the Tabs `size`, `variant`, props
    //     const styles = useMultiStyleConfig('Tabs', tabProps)

    //     return (
    //         <Button __css={styles.tab} {...tabProps}>
    //             <Box as='span' mr='1' display='flex' alignItems='center'>
    //                 {isSelected ? <Image src={pluOn} /> : <Image src={pluOff} />}
    //             </Box>
    //             {/* {tabProps.children} */}
    //         </Button>
    //     )
    // })

    return (
        <ChakraProvider>

            <div style={{ maxWidth: '800px' }}>
                <VStack align='stretch' spacing='-2'>
                    <Box style={{ margin: '10px 10px 30px 10px', paddingLeft: '8px' }}>
                        <FormControl display='flex' alignItems='center'>
                            <FormLabel htmlFor='email-alerts' mb='0'>
                                编辑开关：
                            </FormLabel>
                            <Switch isFocusable size='lg' onChange={() => handleChange()} isChecked={switchStatus} />
                        </FormControl>

                    </Box>

                    <Box>
                        {/* {navCateListData && navCateListData.length > 0 ? (
                            <Tabs variant='enclosed' style={{ width: '900px' }} defaultIndex={tabIndex}>
                                <TabList>
                                    {navCateListData.map((item, index) => {
                                        if (item.id === '-1' && switchStatus) {
                                            return <CustomTab key={`${index}_tab`} onClick={() => addNavItem()}></CustomTab>
                                        }
                                        return <Tab key={`${index}_tab`} onClick={() => switchTab(item, index)}>{item.name}</Tab>
                                    })}
                                </TabList>
                                <TabPanels>
                                    {navCateListData.map((item, index) => (
                                        <TabPanel key={`${index}_tabPanel`} >
                                            {isLoading ? (
                                                <Spinner />
                                            ) : (
                                                <Box>
                                                    <AutoLayout {...config} onItemClick={onNavItemClick} cb={callback} isSwtich={switchStatus} />
                                                </Box>
                                            )}
                                        </TabPanel>
                                    ))}

                                </TabPanels>
                            </Tabs>
                        ) : null} */}

                        {navCateListData && navCateListData.length > 0 ? (
                            <>
                                <TabsCompox items={navCateListData} onSwitchTab={switchTab} isSwtich={switchStatus} cb={tabscallback}/>
                                
                                <div style={{marginTop:'20px'}}>
                                    {isLoading ? (
                                        <Spinner />
                                    ) : (
                                        <Box>
                                            <AutoLayout {...config} onItemClick={onNavItemClick} cb={callback} isSwtich={switchStatus} />
                                        </Box>
                                    )}
                                </div>
                            </>
                        ) : null}

                    </Box>

                </VStack>
            </div>

        </ChakraProvider>
    )

}