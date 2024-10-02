function generate() {
    let length=10
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
    let result = '';
    
    for (let i = 0; i < length; i++) {
      const randomIndex = Math.floor(Math.random() * characters.length);
      result += characters[randomIndex];
    }
  
    return result;
  }
  

for (let i=0;i<100;i++) {

    const reqBody={
        topicPartition: {
            topic: "demotopic3",
            partition: "demopart3"
        },
        key: String(generate()),
        value: String(generate())
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

}