export default function NavBar() {
    return (
        <div className="bg-background text-primary flex  text-2xl px-4 py-4 w-full justify-between">
            <div>
                <img src="Logo.png" className="w-22 absolute animate-spin duration-150" /> <h1 className="ml-32" >WielerWijs</h1>
            </div>
            <div className="mr-32 space-x-12">
                <a href="/teams" className=" text-primary hover:text-white duration-150">Teams beoordelen</a>
                <a href="/" className=" text-primary hover:text-white duration-150">Team maken</a>
            </div>
        </div>
    )
}