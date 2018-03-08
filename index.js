var Discord = require("discord.js");

let client = new Discord.Client();

let mechanism = function mechanism() {
    this.name = "default";
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
        
    }
};

client.on("message", function(message) {
    mechanisms.sort();
    let messageArray = message.content.split(" ");
    let command = messageArray[0];
    if (command === "/add") {
        let newMech = new mechanism();
        newMech.name = messageArray[1];
        mechanisms.add(newMech);
    } else
    if (command === "/showList") {
        let rtn = "\n";
        for (let i = 0; i < mechanisms.size(); i++) {
            rtn += (i+1) + ") " + mechanisms.get(i).name + "\n";
        }
        message.reply(rtn);
    } else
    if (command === "/vote") {
        let number = messageArray[1];
        mechanisms.get(number).votes += 1;
        message.reply(mechanisms.get(number).name + " now has " + mechanisms.get(number).votes + " votes.");
    }
});

client.login('Mzc5Mzc3NTUxMDczNjA3Njgy.DOpKcg.vBirN9G11wc4QSAKh2sUcQYU0io');
