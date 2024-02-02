module.exports = {
  xname: 'Gridbox',
  props: {
      columns: 8
  },
  
  presenter: {
    xname: 'Text',
    props: {
    },
    indicator: {
      // xname: "ShadowIndicator",
      // props: {
      //   borderColor: 'transparent',
      //   boxShadow: '0 0px 4px rgba(0, 0, 0, 0.1)',
      // },
      trigger: 'always'
    },
  },
  binding: {
    "moduleName": "content",
    "componentType": "__indicator.xname",
    "componentProps": "__indicator.props"
  },
  cart: {
    xname: 'Cart',
    props: {
      padding: '30px 10px',
      margin: '1px 0',
      linewidth: "1px",
      corner: '8px',
      // fill: '#edf2f7'
      isHover: false
    },

  },
  container: 'SelectList',
    // bounding: {marginLeft: '10px', marginTop: '5px', padding: '10px', border: '1px solid #EDEFF6'},
    
    // unselector: "", //默认样式
    indicator:
    {
      xname: "ShadowIndicator",
      props: {
      }
    }, //hover 时用, 第一次向子组件转递时,  更名为 hoverIndicator
    selector: {
      xname: "SelectedCartUpperRightIcon",
      props: {
        state: 'selected',
        padding: '0'
      }
    }
};
