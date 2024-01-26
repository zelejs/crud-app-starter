module.exports = {
  xname: 'Gridbox',
  props: {
      columns: 8
  },
  
  presenter: {
    xname: 'IndicatorsItem',
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
    "moduleName": "label",
    "componentType": "__indicator.xname",
    "componentProps": "__indicator.props"
  },
  cart: {
    xname: 'Cart',
    props: {
      padding: '30px 10px',
      margin: '1px 0',
      linewidth: 0,
      corner: '8px',
      // fill: '#edf2f7'
      isHover: false
    },

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
  },
  container: 'SelectList'
};
