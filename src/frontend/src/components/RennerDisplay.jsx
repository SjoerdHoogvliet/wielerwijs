export default function RennerDisplay(props) {
  return (
    <div className="w-lg p-6 rounded-md bg-gray-100 flex">
      <p>{props.renner.naam}</p>
      <p
        onClick={props.function}  
        className=" ml-auto text-2xl hover:cursor-pointer hover:scale-150 duration-150 align-middle"
      > {props.removal ? "-" : "+"} </p>
    </div>
  )
}