export default function RennerDisplay(props) {
  return (
    <div 
        onClick={props.function} 
        className="hover:cursor-pointer aspect-square w-42 h-42 rounded-md bg-gray-100 flex justify-center items-center">
      <h2>{props.renner.naam}</h2>
    </div>
  )
}