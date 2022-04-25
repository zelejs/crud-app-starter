/*
  多选框 全选 反选 不选 工具方法，支持单层和双层数据处理
  参数说明：
  type: String 操作功能
        'all'      全选
        ‘no’       不选
        'reverse'  反选
  checkedItems: Array  已经选中的多选框数据值数组
  allItems: Array      全部可选择的多选框数据值数组
  classItems: Array or null 仅需要处理的一组可选多选框数据值
*/
export const checkBoxTool = (type, checkedItems, allItems, classItems) => {
    let classChecked = []
    if (classItems) {
      // 组内数据处理
      // 计算当前项中已经选择的数据
      classItems.forEach(i => {
        checkedItems.includes(i) && classChecked.push(i)
      })
      // 在全部选择的数据中剔除当前组已选择的数据
      classChecked.forEach(i => {
        checkedItems.includes(i) && (delete checkedItems[checkedItems.indexOf(i)])
      })
      // 删除数据后，需要给已选择数据过滤空
      checkedItems = checkedItems.filter(i => i)
    } else {
      // 全部数据处理
      classItems = allItems
      classChecked = [...checkedItems]
      checkedItems = []
    }
    // 分别处理计算结果
    let res = []
    const actions = {
      all: () => {
        res = [...classItems]
      },
      no: () => {
        res = []
      },
      reverse: () => {
        classItems.forEach(i => !classChecked.includes(i) && res.push(i))
      }
    }
    actions[type]()
    return [...checkedItems, ...res]
  }