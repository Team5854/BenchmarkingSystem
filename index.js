var Discord = require("discord.js");

let client = new Discord.Client();

let mechanism = function mechanism() {
    this.name = "This mechanism does not have a name.";
    this.description = "This mechanism does not have a description.";
    this.votes = 0;
};

let mechanisms = {
    array: [],
    size: function() {
        return this.array.length;
    },
    add: function(newMech) {
        this.array.push(newMech);
    },
    remove: function(i) {
        this.array.splice(i, 1);
    },
    get: function(i) {
        return this.array[i];
    },
    sort: function() {
        let swap = function(arr, i, j){
            var temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        let partition = function(arr, pivot, left, right){
            var pivotValue = arr[pivot].votes,
            partitionIndex = left;

            for(var i = left; i < right; i++){
                if(arr[i].votes > pivotValue){
                    swap(arr, i, partitionIndex);
                    partitionIndex++;
                }
            }
            swap(arr, right, partitionIndex);
            return partitionIndex;
        }
        let quickSort = function(arr, left, right){
            var len = arr.length,
            pivot,
            partitionIndex;


            if(left < right){
                pivot = right;
                partitionIndex = partition(arr, pivot, left, right);
    
                //sort left and right
                quickSort(arr, left, partitionIndex - 1);
                quickSort(arr, partitionIndex + 1, right);
            }
            return arr;
        }
        
        this.array = quickSort(this.array, 0, this.array.length-1);
        
    },
    loadFromFile: function(fileName) {
        
    }
};
let previousCommands = [];
client.on("message", function(message) {
    mechanisms.sort();
    let messageArray = message.content.split(" ");
    let command = messageArray[0];
    if (command === "/add") {
        previousCommands.unshift(messageArray);
        let name = "";
        let description = "";
        let state = -1;
        for (let i = 1; i < messageArray.length; i++) {
            if (messageArray[i] === "name:") {
                state = 0;
            } else if (messageArray[i] === "description:") {
                state = 1;
            } else {
                if (state === 0) {
                    name += messageArray[i] + " ";
                } else if (state === 1) {
                    description += messageArray[i] + " ";
                }
            }
        }
        let newMech = new mechanism();
        if (name != "") {
            newMech.name = name.trim();
        }
        if (description != "") {
            newMech.description
        }
        mechanisms.add(newMech);
    } else
    if (command === "/showList") {
        previousCommands.unshift(messageArray);
        let rtn = "\n ``` \n";
        for (let i = 0; i < mechanisms.size(); i++) {
            rtn += (i+1) + ") " + mechanisms.get(i).name + "\n";
        }
        rtn += "```";
        message.reply(rtn);
    } else
    if (command === "/showFullList") {
        previousCommands.unshift(messageArray);
        let rtn = "\n ``` \n";
        for (let i = 0; i < mechanisms.size(); i++) {
            rtn += (i+1) + ") " + mechanisms.get(i).name + "\n";
            rtn += "\t Description: " + mechanisms.get(i).description + "\n";
            rtn += "\t Votes: " + mechanisms.get(i).votes + "\n";
        }
        rtn += "```";
        message.reply(rtn);
    } else
    if (command === "/upVote") {
        previousCommands.unshift(messageArray);
        let name = messageArray[1];
        let number = null;
        for (let i = 0; i < mechanisms.size(); i++) {
            if (name === mechanisms.get(i).name) {
                number = i;
            }
        }
        if (number != null) {
            mechanisms.get(number).votes += 1;
            if (mechanisms.get(number).votes > 1) {
                message.reply(mechanisms.get(number).name + " now has " + mechanisms.get(number).votes + " votes.");
            } else {
                 message.reply(mechanisms.get(number).name + " now has a vote.");
            }
        } else {
            message.reply("No Mechanism By That Name.");
        }
    } else
    if (command === "/downVote") {
        previousCommands.unshift(messageArray);
        let name = messageArray[1];
        let number = null;
        for (let i = 0; i < mechanisms.size(); i++) {
            if (name === mechanisms.get(i).name) {
                number = i;
            }
        }
        if (number != null) {
            if (mechanisms.get(number).votes > 0) {
                mechanisms.get(number).votes -= 1;
                if (mechanisms.get(number).votes > 1) {
                    message.reply(mechanisms.get(number).name + " now has " + mechanisms.get(number).votes + " votes.");
                } else {
                     message.reply(mechanisms.get(number).name + " now has a vote.");
                }
            } else {
                message.reply(mechanisms.get(number).name + " already has 0 votes.");
            }
        } else {
            message.reply("No Mechanism By That Name.");
        }
    }
    if (command === "/remove" || (previousCommands[0][0] === "/remove" && command === "yes")) {
        previousCommands.unshift(messageArray);
        if (command === "yes") {
            let mechName = mechanisms.get(previousCommands[0][1]-1).name;
            mechanisms.remove(previousCommands[0][1]-1);
            message.reply(mechName + " has been removed")
        } else {
            message.reply("Are You Sure You Want To Remove " + mechanisms.get(messageArray[1]-1).name + "?");
        }
    }
});

client.login('Mzc5Mzc3NTUxMDczNjA3Njgy.DOpKcg.vBirN9G11wc4QSAKh2sUcQYU0io');
