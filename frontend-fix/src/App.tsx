import React, {useEffect, useState} from "react";

const App: React.FC = () => {
    const [flower, setFlower] = useState("none");

    useEffect(() => {
        fetch("/flower")
            .then(response => response.json())
            .then(data => {
                setFlower(data);
            });
    }, []);

    return (
        <div className="App">
            <h1>{JSON.stringify(flower)}</h1>
        </div>
    );
};

export default App;
