function manipulate (currentLine) {
	return currentLine + currentLine + currentLine;
}

function replace (currentLine) {
	currentLine = currentLine.replace("1", "g");
	currentLine = currentLine.replace("2", "g2");
	currentLine = currentLine.replace("3", "g3");
	currentLine = currentLine.replace("4", "g4");
	return currentLine;
}