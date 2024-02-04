module.exports = {
    api: {
      createAPI: '/api/pub/data/services/navCategory',
      getAPI: '/api/pub/data/services/navCategory/(id)',
      updateAPI: '/api/pub/data/services/navCategory/(id)',
      deleteAPI: '/api/pub/data/services/navCategory/(id)'
    },
    fields: [
      {
        label: '类别名',
        field: 'name',
        type: 'input',
        rules: {
          isRequired: true
        },
        props:{
          placeholder: '请输入类别名',
        }
      },
      {
        label: '描述',
        field: 'desc',
        type: 'input',
        rules: {
          isRequired: false
        },
        props:{
          placeholder: '描述属性',
        }
      },
      {
        label: '排序号',
        field: 'sortNum',
        type: 'input',
        rules: {
          isRequired: false
        },
        props:{
          placeholder: '请输入',
        }
      }
    ]
  };
  