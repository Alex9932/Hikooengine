function levelLoaded(sLevelName) {
	print("Loading level: " + sLevelName);
}

function construct() {
	script.get("main.js").addOnloadEvent("levelLoaded");
}

function destruct() {
	
}

function main() {
	
}

function render() {
	
}