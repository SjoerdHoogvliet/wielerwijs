import RennerDisplay from "./RennerDisplay"

export default function TeamDisplay(props) {
        function likeTeam() {
        fetch('http://localhost:8080/api/team/' + props.team.id, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ like: true })
        })
    }

    function dislikeTeam() {
        fetch('http://localhost:8080/api/team/' + props.team.id, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ like: false })
        })
    }

    function removeTeam() {
        fetch('http://localhost:8080/api/team/' + props.team.id, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`,
                'Content-Type': 'application/json'
            }
        })
    }


    if (props.square) {
        return (
            <div 
                className="flex flex-col aspect-square size-64 p-4 rounded-md shadow-md"
            >
                <h2 className="text-2xl text-nowrap text-ellipsis max-w-full overflow-hidden">{props.team.naam}</h2>
                <div className="flex-col space-y-2 pt-2">
                    { props.team.renners &&
                        props.team.renners.map(renner => <div>{renner.naam}</div>)
                    }
                </div>
                <div className="justify-bottom mt-auto flex space-x-4">
                    <button 
                        className="bg-green-500 text-white px-4 py-2 rounded-md hover:cursor-pointer"
                        onClick={() => likeTeam()}
                    >
                        Wijs {props.team.likes}
                    </button>
                    <button
                        className="bg-red-500 text-white px-4 py-2 rounded-md hover:cursor-pointer"
                        onClick={() => dislikeTeam()}
                    >
                        Onwijs {props.team.dislikes}
                    </button>
                </div>
                {
                    props.allowRemoval && <div className="mt-auto justify-end flex space-x-4">
                        <button 
                            className="text-secondary hover:font-bold hover:cursor-pointer duration-150"
                            onClick={() =>removeTeam()}
                        >
                            Verwijderen
                        </button>
                    </div>
                }
            </div>
        )
    } else {
        return (
            <div 
                className="flex-col max-w-full p-4 rounded-md shadow-md"
            >
                <h2 className="text-2xl text-nowrap text-ellipsis max-w-full overflow-hidden">{props.team.naam}</h2>
                <div className="flex space-y-2 pt-2">
                    { props.team.renners &&
                        props.team.renners.map(renner => <span className="ml-1 first:ml-0"><RennerDisplay renner={renner} /></span>)
                    }
                    <div className="justify-end  ml-auto flex space-x-4">
                        <button 
                            className="bg-green-500 text-white px-4 py-2 rounded-md hover:cursor-pointer"
                            onClick={() => likeTeam()}
                        >
                            Wijs {props.team.likes}
                        </button>
                        <button
                            className="bg-red-500 text-white px-4 py-2 rounded-md hover:cursor-pointer"
                            onClick={() => dislikeTeam()}
                        >
                            Onwijs {props.team.dislikes}
                        </button>
                    </div>
                </div>

            </div>
        )
    }
}