module.exports = {
  "xname": "Flexbox",
  "props": {
    "align": "start",
    "direction": "column"
  },
  "presenter": {
    "children": [
      {
          "container": {
            "xname": "TitledContainer",
            "props":{
              "title": "预览"
            }
          },
          "presenter": {
            "xname": "Gridbox",
            "props": {
              "columns": 5,
            },
            "cart": {
              "xname": "Cart",
              "props": {
                "padding": "0px",
                "margin": "0px 20px",
                "linewidth": "0",
                "corner": "8px"
              },
            },
            "binding": {
              "___presenter2": "___presenter",
            },
            "container": "SelectList",
            "mock": [
              {
                "id": 11,
                "url": "https://cdn.photoroom.com/v1/assets-cached.jpg?path=templates_v2%2F675515b8-83d8-45e0-a3ec-7e8c8366ced0%2Fthumb_7C11634D-9BB5-4D1C-932B-06C176EEADE0.jpg",
                "content": '超低质量'
              },
              {
                "id": 21,
                "url": "https://cdn.photoroom.com/v1/assets-cached.jpg?path=templates_v2%2Ff89f434f-9bff-4dd2-99a3-0a724a2f1884%2Fthumb_BE5632B0-CC58-42EC-9A0D-232F0FE8F833.jpg",
                "content": '低质量'
              },
              {
                "id": 31,
                "url": "https://cdn.photoroom.com/v1/assets-cached.jpg?path=templates_v2%2Ff897a52f-ad76-4353-a06d-63a1629294c0%2Fthumb_71558C40-0569-44D5-9F12-AA8F427C5D5D.jpg",
                "content": '普通质量'
              },
              {
                "id": 41,
                "url": "https://cdn.photoroom.com/v1/assets-cached.jpg?path=templates_v2%2F6237b4a2-b067-44a2-9536-9e9387073855%2Fthumb_F5370FEF-1238-4082-843F-95BA3B23138D.jpg",
                "content": '中等质量'
              }
            ],
            "presenter": {},
          }
      },
    ]
  }
}