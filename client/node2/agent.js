
const reqBody={
    nodeurl: "http://localhost",
    nodeport: "8080"
}
const resp=fetch('http://localhost:3000/register', 
{method: 'POST',
headers: {
'Content-Type': 'application/json',
},
body: JSON.stringify(reqBody)
}) 
.then((resp)=>resp.json())
.then((data)=>console.log(data))