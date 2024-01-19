import React, { useEffect, useState } from 'react';
import { ChakraProvider, Tabs, TabList, TabPanels, Tab, TabPanel, Spinner } from '@chakra-ui/react';
import ComponentListPage from '../../composition/componentListPage/index';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

const tabsMap = [
  { id: 1, name: 'Layout', type: 'layout'},
  { id: 2, name: 'Container', type: 'container'},
  { id: 3, name: 'Presenter', type: 'presenter'},
  { id: 4, name: 'Indicator', type: 'indicator'},
  { id: 5, name: 'Selector', type: 'selector'},
  { id: 6, name: 'Cart', type: 'cart'}
]

export default function Index (props) {

  const [tabIndex, setTabIndex] = useState(0);
  const [items, setItems] = useState('');
  const [isLoading, setLoading] = useState(false);

  useEffect(_=>{
    setContent('layout', 0)
  },[])

  function setContent(type, tabIndex){
    setLoading(true)
    setTabIndex(tabIndex)
    getApiUrl(type)
  }

  function getApiUrl(type) {
    setItems([])
    let api =  `/openapi/crud/lc_low_auto_component/lowAutoComponent/lowAutoComponents?componentOption=${type}`
    const queryData = {};
    promiseAjax(api, queryData).then(resp => {
        setLoading(false)
        if (resp && resp.code === 200) {
            setItems(resp.data.records)
        } else {
            console.error("获取数据失败")
        }
    });
  }

  return (
    <ChakraProvider>
        <Tabs margin={'5px 0'} defaultIndex={tabIndex}>
          <TabList>
            { tabsMap.map((tab, index) => (
              <Tab key={`tab_${tab.id}`} onClick={() => setContent(tab.type, index)}>{tab.name}</Tab>
            ))}
          </TabList>

          {/* <TabPanels padding={'0 8px'}>
              <TabPanel padding={'8px'}>
                <ComponentListPage type={type}/>
              </TabPanel>
          </TabPanels> */}
      </Tabs>
      
      <div style={{padding: '0 8px'}}>
        {
          isLoading
          ? <Spinner/>
          : items && items.length > 0 && <ComponentListPage items={items}/>
        }
        </div>
    </ChakraProvider>
  )

}