import { useNavigate } from "react-router";

export default function RennerDisplay(props) {
  let navigate = useNavigate();

  return (
    <div className="w-lg p-6 rounded-md bg-gray-100 flex hover:cursor-pointer" onClick={() => navigate(`/renner/${props.renner.id}`)}>
      <p>{props.renner.naam}</p>
      <p
        onClick={props.function}  
        className=" ml-auto text-2xl hover:cursor-pointer hover:scale-150 duration-150 align-middle"
      > {props.removal ? "-" : "+"} </p>
    </div>
  )
}