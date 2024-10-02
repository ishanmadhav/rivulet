import { Message } from "./types"
import  {generate} from 'random-words'

const message:any="Some random string"




for (let i=0;i<500;i++) {
    const reqBody:Message={
        topicPartition: {
            topic: String(generate()),
            partition: String(generate())
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

