package org.gob.gim.income.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.NamingException;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import ec.gob.gim.income.model.Account;
import ec.gob.gim.income.model.AccountComparator;

@Name("accountList")
public class AccountList{
	
	private Account mainAccount;		
	
	@In(create= true)
	AccountHome accountHome;
	
	private TreeNode<Account> rootNode = null;
	
	private AccountComparator accountComparator;
	
	private List<Account> accounts;
	
	private void addNodes(TreeNode<Account> node, Long id, TreeNode<Account> child) {		
		node.addChild(id, child);
	}
	

	private void startAccounting() {		
		this.rootNode = new TreeNodeImpl<Account>();	
		if(mainAccount == null) mainAccount = findMainAccount();
		this.rootNode.setData(mainAccount);
	}
	
	private Account findMainAccount(){
		if (accounts == null) return null;
		for(Account ac: accounts){
			if(ac.getParent() == null) return ac;
		}
		return null;
	}
	
	
	@BypassInterceptors
	private void loadTree() throws NamingException {		
		accounts = accountHome.findAccounts();
		startAccounting();
		accountComparator = new AccountComparator();
		buildTree(rootNode);
	}
	
	
	private Long idForTree = 1L;
	
	private List<Account> backup = new ArrayList<Account>();
	
	private void buildTree(TreeNode<Account> _rootNode) {	
		Account a=(Account)_rootNode.getData();
		Collections.sort(a.getChildren(), accountComparator);
		for (Account oe:a.getChildren()) {
			if(!backup.contains(oe)) backup.add(oe);		
			TreeNodeImpl<Account> child=new TreeNodeImpl<Account>();
			child.setData(oe);
			buildTree(child);
			addNodes(_rootNode, idForTree, child);
			idForTree++;
		}		
	}
	
	private boolean isFirstTime = true;

	public TreeNode getTreeNode() throws NamingException{
		if(!isFirstTime) return rootNode;
		isFirstTime = false;
		loadTree();		
		return rootNode;
	}

	public void setMainAccount(Account mainAccount) {		
		this.mainAccount = mainAccount;
	}

	public Account getMainAccount() {
		return mainAccount;
	}


	public List<Account> getAccounts() {
		return accounts;
	}


	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

}
