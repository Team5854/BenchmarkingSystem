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
