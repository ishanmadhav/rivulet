const reqBody={
    topicPartition: {
        topic: "demotopicx",
        partition: "demopartx"
    },
    key: "key2",
    value: "val2"
}
const resp=fetch('http://localhost:8080/api/produce', 
{method: 'POST',
headers: {
'Content-Type': 'application/json',
},
body: JSON.stringify(reqBody)
}) 
.then((data)=>data.json())
.then((res)=>console.log(res))