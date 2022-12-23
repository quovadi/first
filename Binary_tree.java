
package com.mycompany.lab4;

class TreeNode<T> {
    
    public int key;
    public T data;
    public TreeNode leftDescendant;
    public TreeNode rightDescendant;
    
        
    TreeNode(int key, T data) {
        this.key = key;
        this.data = data;
    }
    
}

public class Binary_tree<T> {
    
    private TreeNode root;
    private int thickness=1;
    
        
    public Binary_tree(int key, T data) { //дерево создаётся вместе с корнем
        this.root = new TreeNode(key, data);      
    }
    
    public Binary_tree(Binary_tree copy) {
        this.root = new TreeNode(copy.root.key, copy.root.data);
        this.thickness = copy.thickness;
        doppelgänger (this.root, copy.root);
    }
    
    
    public int getThick() {
        return thickness;
    }
    
    public TreeNode getRoot() {
        return root;
    }
    
    public void insert(int pasteKey, T data) {
        TreeNode newNode = new TreeNode(pasteKey, data);
        if (root == null) {
            root = newNode;
            thickness++;
        }
        else {
            TreeNode current = root; //current нужно для сравнения ключей узлов в дереве с новым ключом;
            TreeNode ancestor = root; //ancestor нужно для сохранения ссылки на предка cравниваемого с новым ключом узла;
            while(true) { //по циклу ищем место, куда вставить новый узел, двигаясь от узла то влево, то вправо;
                if (pasteKey == current.key) { //если так вышло, что произошла попытка добавить узел с существующим ключом,
                    current.data = newNode.data;
                    newNode = null;
                    return;
                    /*newNode.leftDescendant = current.leftDescendant; //то новый узел связывается с потомками
                    newNode.rightDescendant = current.rightDescendant; //старого узла;
                    if(current != root) { //доп условие на перезапись не корневого узла:
                        if(ancestor.leftDescendant == current) //происходит связывание предка
                            ancestor.leftDescendant = newNode; //старого узла с новым узлом
                        else ancestor.rightDescendant = newNode; //в зависимости от положения
                    }*/
                }
                ancestor = current;            
                if(pasteKey < current.key) {//если значение нового ключа меньше сравниваемого,
                    current = current.leftDescendant; //поворачиваем налево;
                    if(current == null) {//если найдено вакантное место,
                        ancestor.leftDescendant = newNode;//добавляем новый узел на это место;
                        thickness++;
                        return;
                    }
                }
                else {
                    current = current.rightDescendant;//то же самое для поворота направо
                    if(current == null) {
                        ancestor.rightDescendant = newNode;
                        thickness++;
                        return;
                    }
                }
            }
        }    
    }
    
    TreeNode find(int findKey) {
        TreeNode current = root;
        while (current.key != findKey) {
            if (findKey < current.key)
                current = current.leftDescendant;
            else
                current = current.rightDescendant;
            if (current == null) {
                System.out.println("Элемент с заданным ключом не найден :(");
                return null;
            }
        }
        return current;       
    }
    
    private TreeNode getSucc(TreeNode delNode) {//даёт на выход узел со следующим после delNode ключом;
        TreeNode succParent = delNode;//им будет самый левый из правого поддерева delNode;
        TreeNode succ = delNode;//понадобится при удалении, если у удаляемого узла два потомка;
        TreeNode current = delNode.rightDescendant;
        while (current != null) {//по циклу перебираем всех левых потомков
            succParent = succ;//правого потомка узла delNode (если они есть);
            succ = current;
            current = current.leftDescendant;
        }
        if (succ != delNode.rightDescendant) {//если succ не оказывается правым потомком delNode, а оказывается одним из левых потомков правого потомка,
            succParent.leftDescendant = succ.rightDescendant;//правое поддерево узла succ вставляем на место узла succ;
            succ.rightDescendant = delNode.rightDescendant;//вставить узел succ на место delNode, связав правое поддерево узла delNode с узлом succ;
        }
        return succ;
    }
    
    public void delete(int deleteKey) {
        TreeNode current = root;
        TreeNode ancestorOfRemovable = root;
        boolean isLeftDescendant = true;
        while (current.key != deleteKey) { //цикл на поиск удаляемого узла;
            ancestorOfRemovable = current;//почти копия метода find, только теперь есть доступ 
            if(deleteKey < current.key) {//к предку найденного узла;
                isLeftDescendant = true;
                current = current.leftDescendant;
            }
            else {
                isLeftDescendant = false;
                current = current.rightDescendant;
            }
        }
        if (current == null) {
            System.out.println("элемент с заданным ключом невозможно удалить: его не существует");
            return;
        }
        //нашли удаляемый узел;
        
        /*
        возможны три глобальных случая случая: удаляемый узел — лист (ноль потомков),
        имеет одного потомка или имеет двух потомков; каждую их этих глобальных ситуаций
        надобно по-своему обработать; при этом, каждый из этих случаев делится ещё на три подслучая:
        удаляемый узел — или корень, или правый потомок, или левый потомок;
        */
        
        //случай 1: удаляемый узел не имеет потомков: он просто изничтожается;
        if(current.leftDescendant == null && current.rightDescendant == null) {
            if (current == root) {
                root = null;
            }
            else if(isLeftDescendant)
                ancestorOfRemovable.leftDescendant = null;
            else
                ancestorOfRemovable.rightDescendant = null;
        }    
        
        //cлучай 2: удаляемый узел имеет одного потомка;
        else if (current.leftDescendant == null ^ current.rightDescendant == null) {
            if (current == root)
                root = (current.leftDescendant != null) ? current.leftDescendant : current.rightDescendant;
            else if(isLeftDescendant) 
                ancestorOfRemovable.leftDescendant = (current.leftDescendant != null) ? current.leftDescendant : current.rightDescendant;
            else ancestorOfRemovable.rightDescendant = (current.leftDescendant != null) ? current.leftDescendant : current.rightDescendant;                
        }
        
        //cлучай 3: удаляемый узел имеет двух потомков,
        //в таком случае он готовит три конверта и заменяется преемником
        //(нахождение преемника вынесено в отдельный приватный метод для большей читаемости)
        else {
            TreeNode succ = getSucc(current);
            if(current == root) 
                root = succ;
            else if(isLeftDescendant) 
                ancestorOfRemovable.leftDescendant = succ;
            else
                ancestorOfRemovable.rightDescendant = succ;
            succ.leftDescendant = current.leftDescendant;
        }
        thickness--;           
    }
    
    public void deleteAll(TreeNode subRoot) {
        if(subRoot != null) {
            deleteAll(subRoot.leftDescendant);
            deleteAll(subRoot.rightDescendant);
            delete(subRoot.key);
        }
        thickness = 0;
    }
    
    void printTree(TreeNode subRoot) {
        if (subRoot != null) {
            printTree(subRoot.leftDescendant);
            System.out.printf("%d:[%s] ", subRoot.key, subRoot.data);
            printTree(subRoot.rightDescendant);
        }
    }
    
    private void doppelgänger (TreeNode dest, TreeNode source) { //костыльный метод, использумеый только в конструкторе копирования и созданный исключительно для него же
        /*
        использование рекурсии считаю решением неудачным, так как при копировании условного дерева размером мильон элементов копирование займёт
        примерно две с половиной вечности; к сожалению, более оптимального решения для копирования байнари дерева мне не удалось реализовать(
        */
        if (source != null) { 
            dest.leftDescendant = source.leftDescendant;
            dest.rightDescendant = source.rightDescendant;
            doppelgänger (dest.leftDescendant, source.leftDescendant);
            doppelgänger (dest.rightDescendant, source.rightDescendant);
        }
        
    }
}


