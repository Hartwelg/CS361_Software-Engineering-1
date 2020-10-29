var isSetup = true;
var isSonar = false;
var placedShips = 0;
var numSonars = 2;
var numMoves = 2;
var game;
var shipType;
var vertical;

function makeGrid(table, isPlayer) {
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        let number = document.createElement("nums");
        let newContent = document.createTextNode(9-i);
        number.appendChild(newContent);
        number.setAttribute("id", "hiddencell");
        number.classList.add("numcell");
        row.appendChild(number);
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            column.classList.add("normiecell");
            column.addEventListener("click", cellClick);
            row.appendChild(column);
        }

        table.appendChild(row);
    }
    let lastrow = document.createElement('tr');
    let empty = document.createElement("nums");
    lastrow.appendChild(empty);

    for(i = 0; i < 10; i++){
        let letter = document.createElement("td");
        let newContent = document.createTextNode(String.fromCharCode(i+97));
        letter.appendChild(newContent);
        letter.setAttribute("id", "hiddencell");
        lastrow.appendChild(letter);
     }
     table.appendChild(lastrow);
}

function markHits(board, elementId, surrenderText) {
    document.getElementById("last_action_text").style.color = "black";
    board.attacks.forEach((attack) => {
        let className;
        if (attack.result === "MISS") {
            className = "miss";
        }
        else if (attack.result === "HIT") {
            className = "hit";
        }
        else if (attack.result === "SUNK") {
            className = "sink";
        }
        else if (attack.result === "SURRENDER") {
            document.getElementById("last_action_text").innerHTML = surrenderText;
            className = "sink";
        }
        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);
    });
    if (elementId == "opponent") {
        if (board.attacks[board.attacks.length - 1] != undefined) {
            let attack = board.attacks[board.attacks.length - 1]
            if (attack.result === "MISS") {
                document.getElementById("last_action_text").innerHTML = "Miss at (" + attack.location.column + ", " + (10 - attack.location.row) + ")";
            }
            else if (attack.result === "HIT") {
                document.getElementById("last_action_text").innerHTML = "Hit at (" + attack.location.column + ", " + (10 - attack.location.row) + ")";
            }
            else if (attack.result === "SUNK" || attack.result === "SURRENDER") {
                if(attack.result === "SUNK"){
                    document.getElementById("last_action_text").innerHTML = "Sunk enemy " + attack.ship.kind;
                }
                if (attack.ship.kind == "MINESWEEPER") {
                    document.getElementById("opp_minesweeper").style.backgroundColor = "black";
                    document.getElementById("opp_minesweeper").style.color = "white";
                } else if (attack.ship.kind == "DESTROYER") {
                    document.getElementById("opp_destroyer").style.backgroundColor = "black";
                    document.getElementById("opp_destroyer").style.color = "white";
                } else if (attack.ship.kind == "BATTLESHIP") {
                    document.getElementById("opp_battleship").style.backgroundColor = "black";
                    document.getElementById("opp_battleship").style.color = "white";
                } else if (attack.ship.kind == "SUBMARINE") {
                    document.getElementById("opp_submarine").style.backgroundColor = "black";
                    document.getElementById("opp_submarine").style.color = "white";
                }
            }
        }
    }
}

function redrawGrid() {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    if (game === undefined) {
        return;
    }

    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");
    }));
    // Draw sonar squares
    game.opponentsBoard.sonars.forEach((square) => {
        document.getElementById("opponent").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("gray");
    });
    // Check for occupied squares in sonar
    game.opponentsBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => game.opponentsBoard.sonars.forEach((sonar) => {
        if (square.row == sonar.row && (square.column.charCodeAt(0) - 'A'.charCodeAt(0)) == (sonar.column.charCodeAt(0) - 'A'.charCodeAt(0))) {
            document.getElementById("opponent").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.remove("gray");
            document.getElementById("opponent").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");
        }
    })));
    markHits(game.opponentsBoard, "opponent", "You won the game");
    markHits(game.playersBoard, "player", "You lost the game");
}

var oldListener;
function registerCellListener(f, board) {
    let el = document.getElementById(board);
    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }
    oldListener = f;
}

function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);
    if (isSetup) {
        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;

            redrawGrid();
            placedShips++;
            if (placedShips == 4) {
                isSetup = false;
                //registerCellListener((e) => {});
            }
        });
    } else if (isSonar) {
        isSonar = false;
        sendXhr("POST", "/sonar", {game: game, x: row, y: col, numSonars: numSonars}, function(data) {
            numSonars--;
            game = data;
            redrawGrid();
        })
    }
    else {
        sendXhr("POST", "/attack", {game: game, x: row, y: col}, function(data) {
            game = data;
            redrawGrid();
        })
    }
}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            // Redraw to clear any outstanding event handlers
            redrawGrid();
            document.getElementById("last_action_text").innerHTML = "ERROR";
            document.getElementById("last_action_text").style.color = "red";
            document.getElementById("last_action").style.border = "1px solid red";
            return;
        } else {
            document.getElementById("last_action_text").innerHTML = "";
            document.getElementById("last_action").style.border = "1px solid black";

        }
        handler(JSON.parse(req.responseText));
    });
    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}

function place(size) {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        vertical = document.getElementById("is_vertical").checked;
        let table = document.getElementById("player");
        for (let i=0; i<size; i++) {
            let cell;
            if(vertical) {
                let tableRow = table.rows[row+i];
                if (tableRow === undefined) {
                    // ship is over the edge; let the back end deal with it
                    break;
                }
                cell = tableRow.cells[col];
            } else {
                cell = table.rows[row].cells[col+i];
            }
            if (cell === undefined) {
                // ship is over the edge; let the back end deal with it
                break;
            }
            cell.classList.toggle("placed");
        }
    }
}

function sonar() {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        let radius = 2;
        let table = document.getElementById("opponent");
        let cells = [];
        for (let i = 0; i <= radius; i++) {
            if (table.rows[row+i] !== undefined)
                cells.push(table.rows[row+i].cells[col]);
            if (table.rows[row-i] !== undefined)
                cells.push(table.rows[row-i].cells[col]);
            if (table.rows[row-1] !== undefined) {
                cells.push(table.rows[row-1].cells[col-1]);
                cells.push(table.rows[row-1].cells[col+1]);
            }
            if (table.rows[row+1] !== undefined) {
                cells.push(table.rows[row+1].cells[col-1]);
                cells.push(table.rows[row+1].cells[col+1]);
            }
            cells.push(table.rows[row].cells[col+i]);
            cells.push(table.rows[row].cells[col-i]);
        }
        cells.forEach(function(e) {
            if (e !== undefined) {
                e.classList.toggle("placed");
            }
        });
        isSonar = true;
    }
}

function initGame() {
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
        registerCellListener(place(2), "player");
    });
    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
        registerCellListener(place(3), "player");
    });
    document.getElementById("place_battleship").addEventListener("click", function(e) {
        shipType = "BATTLESHIP";
        registerCellListener(place(4), "player");
    });
        document.getElementById("place_submarine").addEventListener("click", function(e) {
            shipType = "SUBMARINE";
            registerCellListener(place(4), "player");
        });

    document.getElementById("sonar_button").addEventListener("click", function(e) {
        registerCellListener(sonar(), "opponent");
    });
    document.getElementById("move_north").addEventListener("click", function(e) {
        sendXhr("POST", "/check_move", {game: game}, function(data) {
            sendXhr("POST", "/move", {game: game, direction: 0, numMoves: numMoves}, function(data) {
                game = data;
                redrawGrid();
                numMoves--;
            })
        })
    });
    document.getElementById("move_east").addEventListener("click", function(e) {
        sendXhr("POST", "/check_move", {game: game}, function(data) {
            sendXhr("POST", "/move", {game: game, direction: 1, numMoves: numMoves}, function(data) {
                game = data;
                redrawGrid();
                numMoves--;
            })
        })
    });
    document.getElementById("move_south").addEventListener("click", function(e) {
        sendXhr("POST", "/check_move", {game: game}, function(data) {
            sendXhr("POST", "/move", {game: game, direction: 2, numMoves: numMoves}, function(data) {
                game = data;
                redrawGrid();
                numMoves--;
            })
        })
    });
    document.getElementById("move_west").addEventListener("click", function(e) {
        sendXhr("POST", "/check_move", {game: game}, function(data) {
            sendXhr("POST", "/move", {game: game, direction: 3, numMoves: numMoves}, function(data) {
                game = data;
                redrawGrid();
                numMoves--;
            })
        })
    });
    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
};