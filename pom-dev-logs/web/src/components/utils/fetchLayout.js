export default (jsonPath, setJsonObject) => {

    // const data = {};

    fetch(jsonPath ,{
      headers : { 
        'Content-Type': 'application/json',
        'Accept': 'application/json'
       }
    })
      .then(function(resp){
        // console.log("resp = ",resp)
        // data.status = resp.status;
        return resp.json();
      })
      .then(function(myJson) {
        // console.log('myJson = ', myJson);
        // data.jsonObject = myJson;
        setJsonObject(myJson);
        // return myJson
      });
}