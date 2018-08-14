package org.gob.loja.gim.ws.service;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gob.gim.income.action.WorkdayHome;
import org.jboss.seam.annotations.In;

@Stateless(name = "WorkDay")
//@Name("WorkDayWsb")
//@Scope(SESSION)
//@Startup
@Interceptors({SecurityInterceptor.class})
public class WorkDayServiceBean implements WorkDay {

    @PersistenceContext
    EntityManager em;
    
    private Boolean needsInterestRateDefined = Boolean.FALSE;
    private Boolean needsTaxRateDefined = Boolean.FALSE;
    private Boolean closingWorkday = Boolean.FALSE;
    private Boolean existsCurrentWorkday = Boolean.FALSE;
    private Boolean fromIncome = Boolean.FALSE;
    private Boolean existOpenedTills = Boolean.FALSE;
    
    @In(create = true)
    private WorkdayHome who;

    /*@In(create = true)
    private PasswordManager passwordManager = new PasswordManager();

    @In(create = true)
    WorkdayHomeWs who = new WorkdayHomeWs();

    WorkdayList wl = new WorkdayList();

    @In(create = true)
    TillPermissionHomeWs t = new TillPermissionHomeWs();

    @In(create = true)
    Gim gim = new Gim();

//    @In
//    FacesMessages facesMessages = new ;
//    @In
//    Credentials credentials;
//    @Logger
//    private Log log = new ;
//    @In(scope = ScopeType.APPLICATION)
//    Gim gim = new Gim();
    @In(create = true)
    UserSession userSession = new UserSession();

    @In(create = true)
    Identity identity = new Identity();

    private String ip;

//    @SuppressWarnings({"unchecked"})
    private Boolean isUserValid(ServiceRequest request, HttpServletRequest hrs) {
//        String hashPassword = passwordManager.hash();
        String hashPassword = passwordManager.hash(request.getPassword());
//        log.info("Authenticating user {0}, {1}", request.getUsername(), hashPassword);
        //System.out.println("Authenticating user: " + request.getUsername() + " " + hashPassword);
        Query query = em.createNamedQuery("User.findByUsernameAndPassword");
        query.setParameter("name", request.getUsername());
        query.setParameter("password", hashPassword);
        User user = null;
        try {
            user = (User) query.getSingleResult();
        } catch (Exception e) {
//			e.printStackTrace();
//            facesMessages.addFromResourceBundle("security.userAccountOrPasswordInvalid");
            return false;
        }

        if (!user.getIsActive()) {
//            facesMessages.addFromResourceBundle("security.accountIsNotActive");
            return false;
        }
        if (user.getIsBlocked()) {
//            facesMessages.addFromResourceBundle("security.accountWasBlocked");
            return false;
        }

//        log.info("USUARIO OBTENIDO ---> " + user.getName() + " " + user.getPassword());
//        gim.initialize();
        Boolean isSuccessfullyRegistered = registerSession(user, hrs.getSession());
        if (!isSuccessfullyRegistered) {
//            facesMessages.addFromResourceBundle("security.userIsAlreadyLoggedIn", user.getName());
            return true;
        }

        query = em.createNamedQuery("User.findResidentByUserId");
        query.setParameter("userId", user.getId());
        Person person = null;
        try {
            person = (Person) query.getSingleResult();
        } catch (Exception e) {
//            facesMessages.addFromResourceBundle("security.noAccountOwnerAssociated");
            return false;
        }

        userSession.setUser(user);
        userSession.setPerson(person);
        userSession.setJournal(retrieveJournal(person));

        query = em.createNamedQuery("User.findRolesByUserId");
        query.setParameter("userId", user.getId());
        List<Role> roles = query.getResultList();

        if (roles != null) {
            user.setRoles(roles);
            for (Role mr : roles) {
                identity.addRole(mr.getName());
            }
        }
        findIpRemoteMachine(hrs);
        if (isCashier()) {
            TillPermission tp = findTillPermission();
            if (tp != null && isTillAdressCorrect(tp.getTill())) {
                userSession.setTillPermission(tp);
            }
        }
        //System.out.println("USUARIO CONECTADO: " + user.getName());
        return true;
    }

    @SuppressWarnings("unchecked")
    public TillPermission findTillPermission() {
        Date currentDate = new Date();
        Query query = em
                .createNamedQuery("TillPermission.findByPersonIdAndWorkdayDate");
        query.setParameter("personId", userSession.getPerson().getId());
        query.setParameter("date", currentDate);
        List<TillPermission> tillPermissions = query.getResultList();
        if (tillPermissions != null && tillPermissions.size() > 0) {
            if (tillPermissions.get(0).isEnabled()) {
                return tillPermissions.get(0);
            }
        }
        return null;
    }*/

    /*public String findIpRemoteMachine(HttpServletRequest hrs) {
//        ExternalContext context = //javax.faces.context.FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = hrs;
        ip = request.getHeader("X-Forwarded-For");
        if (isIPAddress(ip)) {
      
            return ip;
        } else {
            ip = request.getRemoteAddr();
            return ip;
        }
//		ip = request.getRemoteAddr(); //En algunos casos solamente detecta la IP del Proxy y no la del Equipo Local
//		System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::machine ip found:::::::::::::::::::::::::::::::::: " + ip);
//		return ip;
    }

    /*public boolean isCashier() {
        SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
        String cashierRoleName = systemParameterService.findParameter("ROLE_NAME_CASHIER");

        if (userSession.getUser().hasRole(cashierRoleName)) {
            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public Journal retrieveJournal(Person person) {
        Date currentDate = new Date();
        Query query = em.createNamedQuery("Journal.findCurrent");
        query.setParameter("currentDate", currentDate);
        query.setParameter("operatorId", person.getId());
        for (Journal j : (List<Journal>) query.getResultList()) {
            return j;
        }
        return null;
    }

    public boolean isTillAdressCorrect(Till t) {
        if (t.getAddress().equals(ip)) {
            return true;
        }
        return false;
    }

    private boolean isIPAddress(String ip) {
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        Pattern pattern = null;
        Matcher matcher;

        String IPADDRESS_PATTERN
                = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    @Override
    public Boolean workDay(ServiceRequest request, HttpServletRequest hrs) throws InvalidUser, DateNoAvalible, InterestRateNoDefined, TaxesNoDefined, CashierOpen, ExistsDuplicateUsers, Exception {
        boolean valid = isUserValid(request, hrs);
        if (valid) {
            t = new TillPermissionHomeWs();
            String msjStringSj;
            String msjStringBd;
//            EntityManager aux =  who.getPersistenceContext();
            who = new WorkdayHomeWs();
            who.setPersistenceContext(em);
            gim.setPersistenceContext(em);
            gim.initialize();
            userSession.setPersistenceContext(gim.getPersistenceContext());
            who.setUserSession(userSession);
            gim.loadFiscalPeriod();
            userSession.setGim(gim);
            userSession.loadFiscalPeriod();
            who.checkSettings();
//     defaun true       who.setClosingCurrentWorkday(false);
            who.setFromIncome(who.isFromIncome());
            t.setUserSession(userSession);
            t.setWorkdayHome(who);
            t.setPersistenceContext(who.getPersistenceContext());

            if (who.canCreateNewWorkday()) {
                who.setClosingWorkday(false);
                try {
                    who.wire();
                    if (!who.isNeedsInterestRateDefined() && !who.isNeedsTaxRateDefined()
                            && !who.isExistOpenedTills()) {
                        Calendar current = DateUtils.getTruncatedInstance(Calendar.getInstance().getTime());
                        if (who.isDateAvailable(who.getInstance().getDate()) || (who.getInstance().getDate().before(current.getTime()))) {
                            who.save();
                            openBanks();
                            msjStringSj = "inicio de jornada exitoso";
                            msjStringBd = "jornadala jornada se inicio correctamrnte";
                            sendmail(msjStringSj, msjStringBd);
                        } else {
                            throw new DateNoAvalible();
                        }

                    } else {
                        if (who.isNeedsInterestRateDefined()) {
                            throw new InterestRateNoDefined();
                        } else if (who.isNeedsTaxRateDefined()) {
                            throw new TaxesNoDefined();
                        } else {
                            try {
                                throw new CashierOpen();
                            } catch (CashierOpen ex2) {
                                msjStringSj = "error en inicio de jornada";
                                msjStringBd = "Existen punto(s) de emisión abierto(s)";
                                sendmail(msjStringSj, msjStringBd);
                                ex2.printStackTrace();
                                throw ex2;
                            } catch (Exception ex2) {
                                msjStringSj = "error desconocido";
                                msjStringBd = "jornadano no fue empezada, inicio abortado: " + ex2.getMessage();
                                sendmail(msjStringSj, msjStringBd);
                                ex2.printStackTrace();
                                throw ex2;
                            }

                        }

                    }
                }catch (ExistsDuplicateUsers ex) {
                    msjStringSj = "Error en los usuarios";
                    msjStringBd = "Existen usuarios duplicados";
                    sendmail(msjStringSj, msjStringBd);
                    ex.printStackTrace();
                    throw ex;
                } catch (DateNoAvalible ex) {
                    msjStringSj = "error en la fecha";
                    msjStringBd = "jornada ya empezada en la misma fecha, inicio abortado";
                    sendmail(msjStringSj, msjStringBd);
                    ex.printStackTrace();
                    throw ex;
                } catch (InterestRateNoDefined ex) {
                    msjStringSj = "error en impuestos";
                    msjStringBd = "Falta definir tasas de los impuestos fiscales usados en algunas cuentas";
                    sendmail(msjStringSj, msjStringBd);
                    ex.printStackTrace();
                    throw ex;
                } catch (TaxesNoDefined ex) {
                    msjStringSj = "error en intereses";
                    msjStringBd = "No se ha definido las tasas de interés para la fecha actual";
                    sendmail(msjStringSj, msjStringBd);
                    sendmail(msjStringSj, msjStringBd);
                    ex.printStackTrace();
                    throw ex;
                } catch (Exception ex) {
                    msjStringSj = "error desconocido";
                    msjStringBd = "jornada no pudo iniciarse error: " + ex.getMessage();
                    ex.printStackTrace();
                    sendmail(msjStringSj, msjStringBd);
                    throw ex;
                }
            } else {
                try {
                    who.setClosingWorkday(true);
                    who.wire();
                    if (!who.isNeedsInterestRateDefined() && !who.isNeedsTaxRateDefined()) {
                        if (who.isExistOpenedTills()) {
                            closeTills();
                        }
                        if (!who.isExistOpenedTills()) {
                            who.generateGlobalReport();
                            who.save();
                            msjStringSj = "fin de jornada exitoso";
                            msjStringBd = "jornadala jornada se finalizo correctamrnte";
                            sendmail(msjStringSj, msjStringBd);
                        } else {
                            if (who.isExistOpenedTills()) {
                                try {
                                    throw new CashierOpen();
                                } catch (CashierOpen ex2) {
                                    msjStringSj = "error en fin de jornada";
                                    msjStringBd = "Existen punto(s) de emisión abierto(s)";
                                    sendmail(msjStringSj, msjStringBd);
                                     ex2.printStackTrace();
                                    throw ex2;
                                } catch (Exception ex2) {
                                    msjStringSj = "error desconocido";
                                    msjStringBd = "jornadano no fue finalizada, fin abortado: " + ex2.getMessage();
                                    sendmail(msjStringSj, msjStringBd);
                                     ex2.printStackTrace();
                                    throw ex2;
                                }

                            }
                        }
                    } else {
                        if (who.isNeedsInterestRateDefined()) {

                            throw new InterestRateNoDefined();
                        } else if (who.isNeedsTaxRateDefined()) {

                            throw new TaxesNoDefined();
                        }
                    }

                } catch (InterestRateNoDefined ex) {
                    msjStringSj = "error en intereses";
                    msjStringBd = "Falta definir tasas de los impuestos fiscales usados en algunas cuentas";
                    sendmail(msjStringSj, msjStringBd);
                    ex.printStackTrace();
                    throw ex;
                } catch (TaxesNoDefined ex) {
                    msjStringSj = "error en intereses";
                    msjStringBd = "No se ha definido las tasas de interés para la fecha actual";
                    sendmail(msjStringSj, msjStringBd);
                    ex.printStackTrace();
                    throw ex;
                } catch (Exception ex) {
                    msjStringSj = "error en el cierre";
                    msjStringBd = "jornada no pudo finalizarse error: " + ex.getMessage();
                    ex.printStackTrace();
                    sendmail(msjStringSj, msjStringBd);
                    throw ex;
                }
            }

        }
        return valid;

    }

    private Boolean registerSession(User user, HttpSession Session) {
        User userAux = ((User) Session.getAttribute("user"));
        if (userAux != null && ((User) Session.getAttribute("user")).getId().equals(user.getId())
                && ((User) Session.getAttribute("user")).getPassword().equals(user.getPassword())) {
            return false;
        } else {
            Session.setAttribute("user", user);
            return true;
        }

    }

    private void sendmail(String msjStringSj, String msjStringBd) {
        // Recipient's email ID needs to be mentioned.
        String to = " richardmijo@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "foxalex190@gmail.com";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        final String username = "foxalex190@gmail.com";
        final String password = "Oneaccount";

        // Get the default Session object.
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(msjStringSj);

            // Now set the actual message
            message.setText(msjStringBd);

            // Send message
            Transport.send(message);
            //System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            System.out.println("error al enviar mensaje....: " + mex.getMessage());
        }
    }

    private void openBanks() {
        List<Till> ls = t.loadTillsWs();
        t.initialize();
        t.setUserSession(userSession);
        for (Till l : ls) {
            TillPermissionHomeWs tph = new TillPermissionHomeWs();
            tph.setPersistenceContext(em);
            TillPermission tp = tph.findTp(who.getInstance().getId(), l);
            tp.setObservation(tp.getObservation() == null ? "" : tp.getObservation());
            t.changeTillPermission(tp);
            t.updateOpeningTillPermission();
        }
    }

    private boolean IsOpen(TillPermission tp) {
        String sql = "select t.id from TillPermission t where "
                + "t.id = :id and (t.openingTime is not null and t.closingTime is null) ";
        Query query = em.createQuery(sql);
        query.setParameter("id", tp.getId());
        if (query.getResultList().size() >= 1) {
            return true;
        } else {
            return false;
        }
    }

    private void closeTills() {
        Long id = who.findWorkdayId();
        String sql = "select t from TillPermission t where "
                + "t.workday.id = :id and (t.openingTime is not null and t.closingTime is null) ";
        Query query = em.createQuery(sql);
        query.setParameter("id", id);
        List<TillPermission> ls2 = (List<TillPermission>) query.getResultList();

        for (TillPermission tp : ls2) {
            TillPermissionHomeWs tph = new TillPermissionHomeWs();
            tph.setPersistenceContext(em);
            TillPermission tpn = tph.findTp(id, tp.getTill());
            tpn.setObservation(tpn.getObservation() == null ? "" : tpn.getObservation());
            if (IsOpen(tpn)) {
                t.changeTillPermission(tpn);
                t.updateTillPermission();
//                ct++;
            }
        }
        Long currentId = id;
        if (who.getNumberOfTillOpened(currentId) > 0) {
            who.setExistOpenedTills(true);
        } else {
            who.setExistOpenedTills(false);
        }
    }*/

	@Override
	public Boolean openWorkday() {
		// TODO Auto-generated method stub
		return Boolean.FALSE;
	}

	@Override
	public Boolean closeWorkday() {
		// TODO Auto-generated method stub
		return Boolean.FALSE;
	}

}
