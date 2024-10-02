
const resp=fetch('http://localhost:8080/api/consume?topic=demotopicx&partition=demopartx&offset=2', 
{method: 'GET',
headers: {
'Content-Type': 'application/json',
}
}) 
.then((data)=>data.json())
.then((res)=>console.log(res))

// for (let i=1;i<50;i++) {
//     const resp=fetch('http://localhost:8080/api/consume?topic=demotopic3&partition=demopart3&offset='+i, 
//     {method: 'GET',
//     headers: {
//     'Content-Type': 'application/json',
//     }
//     }) 
//     .then((data)=>data.json())
//     .then((res)=>console.log(res))
// }