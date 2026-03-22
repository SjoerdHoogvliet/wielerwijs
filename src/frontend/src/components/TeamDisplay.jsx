export default function TeamDisplay(props) {
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
            <div className="justify-bottom right-0 flex space-x-4">
                <button 
                    className="bg-green-500 text-white px-4 py-2 rounded-md hover:cursor-pointer"
                    onClick={props.likeTeam}
                >
                    Wijs {props.team.likes}
                </button>
                <button
                    className="bg-red-500 text-white px-4 py-2 rounded-md hover:cursor-pointer"
                    onClick={props.dislikeTeam}
                >
                    Onwijs {props.team.dislikes}
                </button>
            </div>
            <div className="mt-auto justify-end flex space-x-4">
                <button 
                    className="text-secondary hover:cursor-pointer"
                    onClick={props.removeTeam}
                >
                    Verwijderen
                </button>
            </div>
        </div>
    )
}