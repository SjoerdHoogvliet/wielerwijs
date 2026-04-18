import { useNavigate } from "react-router";

export default function RennerDisplay(props) {
  let navigate = useNavigate();

  if (props.card) {
    return (
      <div 
        className="w-lg p-6 rounded-md bg-gray-100 flex" 
        
      >
        <p 
          className="hover:cursor-pointer"
          onClick={() => navigate(`/renner/${props.renner.id}`)}
        >
          {props.renner.naam}
        </p>
        <p
          onClick={props.function}  
          className=" ml-auto text-2xl hover:cursor-pointer hover:scale-150 duration-150 align-middle"
        > {props.removal ? "-" : "+"} </p>
      </div>
    )
  } else {
    return (
      <span
        className="text-secondary hover:font-bold duration-150 hover:cursor-pointer"
        onClick={() => navigate(`/renner/${props.renner.id}`)}
      >
        {props.renner.naam}
      </span>
    )
  }
}