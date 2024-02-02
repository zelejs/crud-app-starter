module.exports = {
  xname: 'Wrap',
  props: {
  },
  presenter: {
    xname: 'Flexbox',
    props: {
      direction: 'start',
      justify: 'center row'
    },
    children: [
      // {
      //   presenter: 'DefaultAvatar',
      //   binding: {
      //     url: 'url'
      //   },
      // },
      {
        presenter: "Text",
      }
    ],
  },
  
  binding: {
    "moduleName": "content",
    "componentType": "__cart.xname",
    "componentProps": "__cart.props"
  },
  cart: {
    // xname: 'Cart',
    // props: {
    //   // fill:'#ccc',
    //   // fillHover:'#FFFBE5',
    //   padding: '12px 25px',
    //   margin: '5px',
    //   linewidth: '0'
    // }
  },
  bounding: {margin: '5px', padding: '6px 10px', border: '1px solid #EDEFF6'},
  indicator:{
    xname:'ManageMenuIndicator',
    props:{
      action: {
        // deleteAPI: '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/(id)'
      }
    },
    binding: {
      "id":"id",
      "path":"url",
      "name":"content",
    }
  },
  container: {
    xname: 'ManageList',
    props:{
      addnew: ''
    }
  },
  navigation: {
    model: {
      api: {
        createAPI: '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules',
        getAPI: '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/(id)',
        updateAPI: '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/(id)',
        deleteAPI: '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/(id)'
      },
      fields: [
        // {
        //   label: '显示名称',
        //   field: 'name',
        //   type: 'input',
        //   rules: {
        //     isRequired: true
        //   },
        //   props:{
        //     placeholder: '显示名称',
        //   }
        // },
        // {
        //   label: '组件标识',
        //   field: 'moduleName',
        //   type: 'input',
        //   rules: {
        //     isRequired: true
        //   },
        //   props: {
        //     placeholder: '组件标识'
        //   }
        // },
        // {
        //   label: '标准组件',
        //   field: 'componentId',
        //   type: 'select-fetch',
        //   rules: {
        //     isRequired: true
        //   },
        //   props: {
        //     placeholder: '选择标准组件'
        //   },
        //   saveData:{ //额外提交的字段和值
        //     componentOption: 'componentOption',
        //     componentType: 'componentType'
        //   },
        //   options: {
        //     api: '/openapi/crud/lc_low_auto_component/lowAutoComponent/lowAutoComponents?componentOption=(componentOption)&pageNum=1&pageSize=100',
        //     label: 'componentName',
        //     value: 'id',
        //   }
        // },
      ]
    }
  }
};
